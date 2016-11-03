import {Component, SimpleChanges, OnChanges} from '@angular/core';
import {User} from "../../models/user.model";
import {AuthService} from "../../services/auth.service";
import {TelaService} from "../../services/tela.service";
import {Scheduled} from "../../models/scheduled.model";

@Component({
    selector: 'user-details',
    inputs: ['user'],
    styleUrls: ['./user-details.component.css'],
    templateUrl: './user-details.component.html'
})
export class UserDetailsComponent implements OnChanges {

    private token: string;

    user: User;
    isScheduledLoading: boolean;
    scheduled: Scheduled;

    constructor(private auth: AuthService, private tela: TelaService) {
        this.token = this.auth.getToken();
        this.isScheduledLoading = true;
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes['user']) {
            this.isSelfScheduled();
        }
    }

    isSelfScheduled() {
        this.tela
            .getScheduled(this.token)
            .finally(() => this.isScheduledLoading = false)
            .subscribe(
                (scheduled: Scheduled[]) => {
                    let selfArray = scheduled
                        .filter(scheduled => scheduled.module === 'instagram' && scheduled.action === 'self');
                    this.scheduled = selfArray.length ? selfArray[0] : null;
                }
            );
    }

    watch() {
        this.isScheduledLoading = true;
        this.tela
            .schedule(this.token)
            .finally(() => this.isScheduledLoading = false)
            .subscribe((scheduled) =>this.scheduled = scheduled);
    }

    cancelWatch() {
        this.isScheduledLoading = true;
        this.tela
            .cancel(this.token, this.scheduled.id)
            .finally(() => this.isScheduledLoading = false)
            .subscribe(() =>this.scheduled = null);
    }

}