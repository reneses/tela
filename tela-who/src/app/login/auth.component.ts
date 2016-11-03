import {Component, OnInit} from '@angular/core';
import {AuthService} from '../services/auth.service';
import {TelaService} from '../services/tela.service';
import {User} from '../models/user.model';
import {Router} from '@angular/router';

@Component({
    selector: 'login',
    template: `
        <div class="full">
            <div class="ui active inverted dimmer">
                <div class="ui big text loader"></div>
            </div>
        </div>
    `
})
export class AuthComponent implements OnInit {

    constructor(private auth:AuthService, private router:Router, private tela:TelaService) {
    }

    private static retrieveInstagramTokenFromUrl():string {
        let hash = location.hash;
        if (hash == null)
            return null;
        let tokenParam = hash.split('=');
        if (tokenParam.length != 2 || tokenParam[1] == null)
            return null;
        return tokenParam[1];
    }

    ngOnInit():void {
        let token = AuthComponent.retrieveInstagramTokenFromUrl();
        if (token == null) {
            this.router.navigate(['login']);
        }
        this.tela
            .create(token)
            .subscribe((token:string) => {
                this.tela
                    .self(token)
                    .subscribe((user:User) => {
                        this.auth.logIn(user, token);
                        this.router.navigate(['']);
                    });
            });
    }

}