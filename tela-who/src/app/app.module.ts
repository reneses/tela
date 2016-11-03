import {NgModule} from '@angular/core';
import {HttpModule} from "@angular/http";
import {BrowserModule}  from '@angular/platform-browser';

import {
    routing,
    appRoutingProviders
}  from './app.routing';

import {AppComponent} from './app.component';

import {WhoComponent} from './who/who.component';
import {ToolbarComponent} from './who/toolbar/toolbar.component';
import {DashboardComponent} from './who/dashboard/dashboard.component';
import {UserRowComponent} from './who/dashboard/user-row.component';

import {LoginComponent} from './login/login.component';
import {AuthComponent} from './login/auth.component';

import {AuthService} from "./services/auth.service";
import {TelaService} from "./services/tela.service";
import {UserDetailsComponent} from "./who/dashboard/user-details.component";
import {Config} from "./app.config";

@NgModule({
    imports: [
        BrowserModule,
        HttpModule,
        routing
    ],
    declarations: [
        AppComponent,
        WhoComponent,
        ToolbarComponent,
        DashboardComponent,
        UserRowComponent,
        UserDetailsComponent,
        LoginComponent,
        AuthComponent,
    ],
    providers: [
        appRoutingProviders,
        AuthService,
        TelaService,
        Config
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}