import {Component} from '@angular/core';
import {Location} from '@angular/common';
import {User} from '../../models/user.model';
import {TelaService} from "../../services/tela.service";
import {AuthService} from "../../services/auth.service";
import {ActivatedRoute} from "@angular/router";

@Component({
    selector: 'dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent {

    private token: string;

    loading: boolean;

    following: User[];
    followers: User[];
    friends: User[];

    resultUsers: User[];
    filteredResultUsers: User[];

    showingFollowing: boolean;
    showingFollowers: boolean;
    showingFriends: boolean;

    showingSearch: boolean;
    searchUsername: string;

    /**
     * Constructor. Store the token fro the session and hide all
     *
     * @param auth AuthService
     * @param tela TelaService
     * @param route
     * @param location
     */
    constructor(private auth: AuthService, private tela: TelaService, private route: ActivatedRoute, private location: Location) {
        this.token = this.auth.getToken();
        this.hideAll();
        let params = this.route.snapshot.params;
        if (params.hasOwnProperty('username'))
            this.search(params['username']);
    }

    /**
     * Hide all the components
     */
    hideAll(): void {
        this.loading = false;
        this.showingFollowing = this.showingFollowers = this.showingFriends = false;
        this.resultUsers = this.filteredResultUsers = null;
        this.showingSearch = false;
    }

    //------------------------------- Relationships -------------------------------//

    /**
     * Show the followers of the logged user
     */
    showFollowers(): void {
        this.hideAll();
        if (!this.followers) {
            this.loading = true;
            this.tela
                .followers(this.token)
                .finally(() => this.loading = false)
                .subscribe(
                    followers => {
                        this.followers = followers;
                        this.filteredResultUsers = this.resultUsers = followers;
                        this.showingFollowers = true;
                    }
                );
        }
        else {
            this.filteredResultUsers = this.resultUsers = this.followers;
            this.showingFollowers = true;
        }
    }

    /**
     * Show the users the logged user is following
     */
    showFollowing(): void {
        this.hideAll();
        if (!this.following) {
            this.loading = true;
            this.tela
                .following(this.token)
                .finally(() => this.loading = false)
                .subscribe(
                    following => {
                        this.following = following;
                        this.filteredResultUsers = this.resultUsers = following;
                        this.showingFollowing = true;
                    }
                );
        }
        else {
            this.filteredResultUsers = this.resultUsers = this.following;
            this.showingFollowing = true;
        }
    }

    /**
     * Show the friends of the logged user
     */
    showFriends(): void {
        this.hideAll();
        if (!this.friends) {
            this.loading = true;
            this.tela
                .friends(this.token)
                .finally(() => this.loading = false)
                .subscribe(
                    friends => {
                        this.friends = friends;
                        this.filteredResultUsers = this.resultUsers = friends;
                        this.showingFriends = true;
                    }
                );
        }
        else {
            this.filteredResultUsers = this.resultUsers = this.friends;
            this.showingFriends = true;
        }
    }

    /**
     * Filter the list of results from a relationship
     * @param query
     */
    filterRelationship(query: string): void {
        if (!query)
            this.filteredResultUsers = this.resultUsers;
        else
            this.filteredResultUsers = this.resultUsers.filter(user =>
                user.username.toLowerCase().indexOf(query) >= 0
                || user.fullName.toLowerCase().indexOf(query) >= 0
            );
    }


    //------------------------------- User -------------------------------//

    /**
     * Search the details of an user
     *
     * @param username
     */
    search(username: string): void {
        if (!username) {
            this.location.replaceState('dashboard/');
            this.showingSearch = false;
            return;
        }
        this.location.replaceState('dashboard/user/' + username);
        this.user(username);
    }

    user(username: string) : void {
        this.hideAll();
        this.searchUsername = username;
        this.showingSearch = true;
    }

}
