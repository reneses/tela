export class UserRelationship {

    // Incoming
    static FOLLOWED_BY = 'followed_by';
    static REQUESTED_BY = 'requested_by';
    static BLOCKED = 'blocked_by_you';

    // Outgoing
    static FOLLOWS = 'follows';
    static REQUESTED = 'requested';

    static NONE = 'none';

    constructor(public incoming: string, public outgoing: string, public targetIsPrivate: boolean) {
    }

    targetIsFollower(): boolean {
        return this.incoming.toLowerCase() == UserRelationship.FOLLOWED_BY;
    }

    targetIsBlocked(): boolean {
        return this.incoming.toLowerCase() == UserRelationship.BLOCKED;
    }

    targetHasRequestedFollow(): boolean {
        return this.incoming.toLowerCase() == UserRelationship.REQUESTED_BY;
    }

    originIsFollowing(): boolean {
        return this.outgoing.toLowerCase() == UserRelationship.FOLLOWS;
    }

    originHasRequestedFollow(): boolean {
        return this.outgoing.toLowerCase() == UserRelationship.REQUESTED;
    }

    areFriends(): boolean {
        return this.targetIsFollower() && this.originIsFollowing();
    }

}