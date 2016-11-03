import {Component} from '@angular/core';
import { Router } from '@angular/router';

import {AuthService} from "../../services/auth.service";
import {User} from "../../models/user.model";

@Component({
    selector: 'toolbar',
    templateUrl: './toolbar.component.html',
    styleUrls: ['./toolbar.component.css']
})
export class ToolbarComponent {

    username: string;
    picture: string;

    constructor(private auth: AuthService, private router: Router) {

        let user: User = this.auth.getUser();
        if (user == null)
            return;

        this.username = user.fullName;
        this.picture = user.picture;

    }

    logOut(): void {
        this.auth.logOut();
        this.router.navigate(['login']);
    }
}
