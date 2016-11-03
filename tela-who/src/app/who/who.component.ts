import {Component} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";

@Component({
    selector: 'who',
    template: `
      <toolbar></toolbar>
      <dashboard></dashboard>
    `
})
export class WhoComponent {

    constructor(private auth:AuthService, private router:Router) {
        if (!this.auth.isLogged()) {
            this.router.navigate(['login']);
        }
    }

}