import {Component} from '@angular/core';
import {Location} from '@angular/common';
import {AuthService} from '../services/auth.service';
import {Config} from "../app.config";

@Component({
    selector: 'login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent {

    instagramAuthorizationUrl: string;

    constructor(private auth: AuthService, private config: Config) {

        let clientId = config.instagram.clientId;
        let redirectUri = Location.joinWithSlash(config.instagram.redirectHost, 'auth');
        
        this.instagramAuthorizationUrl = 'https://api.instagram.com/oauth/authorize/?' +
            'client_id=' + clientId +
            '&redirect_uri=' + redirectUri +
            '&response_type=token' +
            '&scope=public_content+follower_list';

    }

}