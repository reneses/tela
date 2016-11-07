import {Component, OnChanges, SimpleChanges} from '@angular/core';
import {User} from '../../models/user.model';
import {UserRelationship} from "../../models/user-relationship.model";
import {TelaService} from "../../services/tela.service";
import {AuthService} from "../../services/auth.service";
import {Media} from "../../models/media.model";

@Component({
    selector: 'user-details',
    inputs: ['username'],
    styleUrls: ['./user-details.component.css'],
    templateUrl: './user-details.component.html'
})
export class UserDetailsComponent implements OnChanges {

    private token:string;

    readonly numberOfMedia = 20;
    private numberOfProcessedMedia:number;

    isUserLoading:boolean;
    user: User;
    error:string;

    relationshipMessage: string;
    media: Media[];
    isMediaLoading:boolean;


    constructor(private auth:AuthService, private tela:TelaService) {
        this.token = this.auth.getToken();
        this.media = [];
        this.isUserLoading = this.isMediaLoading = false;
    }

    ngOnChanges(changes:SimpleChanges):void {
        if (changes['username']) {
            let username = changes['username']['currentValue'];
            this.getUser(username);
        }
    }

    getUser(username:string):void {
        this.user = this.error = null;
        this.isUserLoading = true;
        this.tela
            .user(this.token, username)
            .finally(() => this.isUserLoading = false)
            .subscribe(
                (user:User) => {
                    this.user = user;
                    this.relationship(username);
                    this.getSelfMedia();
                },
                err => {
                    if (err.status == 403)
                        this.error = `The user @${username} is private`;
                    else
                        this.error = 'The user could not be displayed!';
                }
            );
    }

    /**
     * Query the relationship of a user with the logged user
     *
     * @param username
     */
    private relationship(username:string):void {
        this.tela
            .relationship(this.token, username)
            .subscribe((relationship:UserRelationship) => this.updateRelationshipMessage(relationship))
    }


    private updateRelationshipMessage(relationship: UserRelationship): void {

        if (!this.user || !relationship)
            return;
        
        if (relationship.areFriends()) {
            this.relationshipMessage = `@${this.user.username} and you are friends`;
            return;
        }

        let message = '';
        
        if (relationship.originIsFollowing())
            message = `You are following @${this.user.username}.`;
        else if (relationship.originHasRequestedFollow())
            message = `You have requested to follow @${this.user.username}.`;
        
        if (relationship.targetHasRequestedFollow())
            message = `@${this.user.username} requested to follow you.`;
        else if (relationship.targetIsFollower())
            message = `@${this.user.username} is following you.`;
        else if (relationship.targetIsBlocked())
            message = `You blocked @${this.user.username}.`;
        
        if (!message)
            message = `@${this.user.username} and you are not connected.`;

        this.relationshipMessage = message;

    }

    private getSelfMedia():void {
        this.isMediaLoading = true;
        this.tela
            .selfMedia(this.token, this.numberOfMedia)
            .subscribe((media:Media[]) => this.processMedia(media))
    }

    private processMedia(media:Media[]):void {
        this.numberOfProcessedMedia = 0;
        media.forEach(m => {
            this.tela
                .likes(this.token, m.id)
                .subscribe((likes: User[]) => {
                    if (likes.find(user => user.id == this.user.id)) {
                        this.media.push(m);
                    }
                    this.numberOfProcessedMedia++;
                    if (this.numberOfProcessedMedia == media.length)
                        this.isMediaLoading = false;
                })
        });

    }

}