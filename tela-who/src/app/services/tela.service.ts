import {Injectable} from '@angular/core';
import {Location} from '@angular/common';
import {Http, Response, Headers, URLSearchParams, RequestOptions} from '@angular/http';
import {Observable} from "rxjs/Rx";
import {User} from "../models/user.model";
import {UserRelationship} from "../models/user-relationship.model";
import {Media} from "../models/media.model";
import {Config} from "../app.config";

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

    private static parseMedia(response: Object): Media {
        return new Media({
            id: response['id'],
            link: response['link'],
            images: response['images'],
            caption: response['caption']['text'],
            createdAt: new Date(response['created_time'] * 1000)
        });
    }

    private postUserRequest(endpoint: string, token: string): Observable<User> {
        let url = Location.joinWithSlash(this.url, endpoint);
        return this.http
            .get(url, new RequestOptions({headers: TelaService.getAuthorizedHeaders(token)}))
            // .map(res => { console.log(res); return res; })
            .map(res => res.json())
            .map(TelaService.parseUser)
            .catch(this.handleError);
    }


    private postUsersRequest(endpoint: string, token: string): Observable<User[]> {
        let url = Location.joinWithSlash(this.url, endpoint);
        return this.http
            .get(url, new RequestOptions({headers: TelaService.getAuthorizedHeaders(token)}))
            // .map(res => {console.log(res); return res; })
            .map(res => res.json())
            .map((res: Object[]) => res.map(TelaService.parseUser))
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
        return this.postUserRequest('action/instagram/self', token)
    }

    user(token: string, username: string): Observable<User> {
        return this.postUserRequest('action/instagram/user?username=' + username, token)
    }

    following(token: string): Observable<User[]> {
        return this.postUsersRequest("action/instagram/following", token);
    }

    followers(token: string): Observable<User[]> {
        return this.postUsersRequest("action/instagram/followers", token);
    }

    friends(token: string): Observable<User[]> {
        return this.postUsersRequest("action/instagram/friends", token);
    }

    relationship(token: string, username: string): Observable<UserRelationship> {
        let url = Location.joinWithSlash(this.url, 'action/instagram/relationship?username=' + username);
        return this.http
            .get(url, new RequestOptions({headers: TelaService.getAuthorizedHeaders(token)}))
            .map(res => res.json())
            .map(data =>
                new UserRelationship(data['incoming_status'], data['outgoing_status'], data['target_user_is_private'])
            )
            .catch(this.handleError);
    }

    selfMedia(token: string, limit: number): Observable<Media[]> {
        let url = Location.joinWithSlash(this.url, 'action/instagram/self-media?limit=' + limit);
        return this.http
            .get(url, new RequestOptions({headers: TelaService.getAuthorizedHeaders(token)}))
            .map(res => res.json())
            .map((res: Object[]) => res.map(TelaService.parseMedia))
            .catch(this.handleError);
    }

    likes(token: string, mediaId: string): Observable<User[]> {
        return this.postUsersRequest('action/instagram/likes?mediaId=' + mediaId, token);
    }

}