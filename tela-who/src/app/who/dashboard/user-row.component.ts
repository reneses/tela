import {Component, EventEmitter} from '@angular/core';
import {User} from '../../models/user.model';

@Component({
    selector: 'user-row',
    inputs: ['user'],
    outputs: ['detail'],
    host: {
        'class': 'ui segment feed',
        style: 'display: block'
    },
    templateUrl: './user-row.component.html'
})
export class UserRowComponent {
    
    user: User;
    detail: EventEmitter<string>;

    constructor() {
        this.detail = new EventEmitter<string>();
    }

    detailUser(username: string): void {
        this.detail.emit(username);
    }
}