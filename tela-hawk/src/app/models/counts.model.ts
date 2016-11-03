export class Counts {

    public createdAt: Date;

    constructor(public numberOfMedia: number, public numberOfFollowers: number, public numberOfFollowing: number,
                createdAt: number) {

        this.createdAt = new Date(createdAt);
    }

}