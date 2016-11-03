import {Component} from '@angular/core';
import {User} from '../../models/user.model';
import {TelaService} from "../../services/tela.service";
import {AuthService} from "../../services/auth.service";
import {Counts} from "../../models/counts.model";


@Component({
    selector: 'dashboard',
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent {

    private token: string;

    user: User;
    isLoadingUser: boolean;
    counts: Counts[];

    /**
     * Constructor. Store the token fro the session and hide all
     *
     * @param auth AuthService
     * @param tela TelaService
     * @param route
     * @param location
     */
    constructor(private auth: AuthService, private tela: TelaService) {
        this.token = this.auth.getToken();
        let userId = auth.getUser().id;
        this.getUser(userId);
        this.getCounts(userId);
    }

    getUser(userId:number):void {
        this.user = null;
        this.isLoadingUser = true;
        this.tela
            .userById(this.token, userId)
            .finally(() => this.isLoadingUser = false)
            .subscribe((user:User) =>this.user = user);
    }

    getCounts(userId:number):void {
        this.user = null;
        this.isLoadingUser = true;
        this.tela
            .counts(this.token, userId)
            .subscribe(counts => this.counts = counts);
    }

}
