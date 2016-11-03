import {Injectable} from '@angular/core';
import {Location} from '@angular/common';
import {Http, Response, Headers, URLSearchParams, RequestOptions} from '@angular/http';
import {Observable} from "rxjs/Rx";
import {User} from "../models/user.model";
import {Counts} from "../models/counts.model";
import {Config} from "../app.config";
import {Scheduled} from "../models/scheduled.model";

@Injectable()
export class TelaService {

    private url: string;

    constructor(private http: Http, private config: Config) {
        this.url = config.tela.server;
    }

    private static getHeaders(): Headers {
        return new Headers({'Content-Type': 'application/x-www-form-urlencoded'});
    }

    private static getAuthorizedHeaders(token: string): Headers {
        let headers = TelaService.getHeaders();
        headers.set("Authorization", 'Bearer ' + token);
        return headers;
    }

    private static encodeParams(params: Object): string {
        let urlSearchParams = new URLSearchParams();
        for (let key in params) {
            urlSearchParams.append(key, params[key]);
        }
        return urlSearchParams.toString();
    }

    private handleError(error: any) {
        let errMsg = (error.message) ? error.message :
            error.status ? `${error.status} - ${error.statusText}` : 'Server error';
        console.error(errMsg);
        return Observable.throw(error);
    }

    private static parseUser(response: Object): User {
        let obj = {
            id: response['id'],
            username: response['username'],
            fullName: response['full_name'],
            picture: response['profile_picture'],
            bio: response['bio'],
            website: response['website']
        };
        if (response['counts']) {
            obj['following'] = response['counts']['follows'];
            obj['followers'] = response['counts']['followed_by'];
            obj['media'] = response['counts']['media'];
        }
        return new User(obj);
    }

    private getUserRequest(endpoint: string, token: string): Observable<User> {
        let url = Location.joinWithSlash(this.url, endpoint);
        return this.http
            .get(url, new RequestOptions({headers: TelaService.getAuthorizedHeaders(token)}))
            // .map(res => { console.log(res); return res; })
            .map(res => res.json())
            .map(TelaService.parseUser)
            .catch(this.handleError);
    }


    //--------------------------- Tela methods ---------------------------//

    test(): void {
        this.http
            .request(Location.joinWithSlash(this.url, 'test'))
            .subscribe((res: Response) => {
                console.log('Response', res.toString());
            });
    }

    create(token: string): Observable<String> {
        let url = Location.joinWithSlash(this.url, 'auth');
        let params = TelaService.encodeParams({
            module: 'instagram',
            token: token
        });
        return this.http
            .post(url, params, new RequestOptions({headers: TelaService.getHeaders()}))
            // .map(res => { console.log(res); return res; })
            .map(res => res.text())
            .catch(this.handleError);
    }

    self(token: string): Observable<User> {
        return this.getUserRequest('action/instagram/self', token)
    }

    user(token: string, username: string): Observable<User> {
        return this.getUserRequest('action/instagram/user?username=' + username, token)
    }

    userById(token: string, userId: number): Observable<User> {
        return this.getUserRequest('action/instagram/user?userId=' + userId, token)
    }

    getScheduled(token: string): Observable<Scheduled[]> {
        let url = Location.joinWithSlash(this.url, 'schedule');
        return this.http
            .get(url, new RequestOptions({headers: TelaService.getAuthorizedHeaders(token)}))
            .map(res => res.json())
            .catch(this.handleError);
    }

    schedule(token: string): Observable<Scheduled> {
        let url = Location.joinWithSlash(this.url, 'schedule/instagram/self');
        return this.http
            .get(url, new RequestOptions({headers: TelaService.getAuthorizedHeaders(token)}))
            .map(res => res.json()['scheduledAction'])
            .catch(this.handleError);
    }

    cancel(token: string, scheduledId: number): Observable<string> {
        let url = Location.joinWithSlash(this.url, 'schedule/' + scheduledId);
        return this.http
            .delete(url, new RequestOptions({headers: TelaService.getAuthorizedHeaders(token)}))
            .catch(this.handleError);
    }

    counts(token: string, userId: number): Observable<Counts[]> {
        let url = Location.joinWithSlash(this.url, 'action/instagram/counts?userId=' + userId);
        return this.http
            .get(url, new RequestOptions({headers: TelaService.getAuthorizedHeaders(token)}))
            .map(res => res.json().map((obj: Object) =>
                new Counts(obj['media'], obj['followed_by'], obj['follows'], obj['created_at'])))
            .catch(this.handleError);
    }

}