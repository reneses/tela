import {NgModule} from '@angular/core';
import {HttpModule} from "@angular/http";
import {BrowserModule}  from '@angular/platform-browser';

import {
    routing,
    appRoutingProviders
}  from './app.routing';

import {AppComponent} from './app.component';

import {HawkComponent} from './hawk/hawk';
import {ToolbarComponent} from './hawk/toolbar/toolbar.component';
import {DashboardComponent} from './hawk/dashboard/dashboard.component';
import {LoginComponent} from './login/login.component';
import {AuthComponent} from './login/auth.component';

import {AuthService} from "./services/auth.service";
import {TelaService} from "./services/tela.service";
import {UserDetailsComponent} from "./hawk/dashboard/user-details.component";
import {Config} from "./app.config";
import {GraphComponent} from "./hawk/dashboard/graph.component";


@NgModule({
    imports: [
        BrowserModule,
        HttpModule,
        routing
    ],
    declarations: [
        AppComponent,
        HawkComponent,
        ToolbarComponent,
        DashboardComponent,
        UserDetailsComponent,
        LoginComponent,
        AuthComponent,
        GraphComponent,
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