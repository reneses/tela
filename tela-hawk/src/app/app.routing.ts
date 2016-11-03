import {ModuleWithProviders} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';

import {WhoComponent} from './hawk/hawk';
import {LoginComponent} from './login/login.component';
import {AuthComponent} from "./login/auth.component";

const appRoutes: Routes = [
    {path: '', redirectTo: '/dashboard', pathMatch: 'full'},
    {
        path: 'dashboard', children: [
        {
            path: 'user', children: [
                {path: ':username', component: WhoComponent},
                {path: '', redirectTo: '/dashboard', pathMatch: 'full'},
            ]
        },
        {path: '', component: WhoComponent},
    ]
    },
    {path: 'login', component: LoginComponent},
    {path: 'auth', component: AuthComponent}
];
export const appRoutingProviders: any[] = [];
export const routing: ModuleWithProviders = RouterModule.forRoot(appRoutes);