import {Injectable} from '@angular/core';
import {User} from "../models/user.model";

@Injectable()
export class AuthService {

    static readonly USER_KEY: string = 'user';
    static readonly TOKEN_KEY: string = 'token';

    private setUser(user: User): void {
        localStorage.setItem(AuthService.USER_KEY, JSON.stringify(user));
    }

    getUser(): User {
        return JSON.parse(localStorage.getItem(AuthService.USER_KEY));
    }

    private setToken(token: string): void {
        localStorage.setItem(AuthService.TOKEN_KEY, JSON.stringify(token));
    }

    getToken(): string {
        return JSON.parse(localStorage.getItem(AuthService.TOKEN_KEY));
    }

    logIn(user: User, token: string): void {
        this.setUser(user);
        this.setToken(token);
    }

    logOut(): void {
        this.setToken(null);
        this.setUser(null);
    }

    isLogged(): boolean {
        return this.getToken() != null && this.getUser() != null;
    }

}