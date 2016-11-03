export interface Scheduled {

    readonly id: number;
    readonly delay: number;
    readonly accessToken: number;
    readonly params: string[];
    readonly createdAt: number;
    readonly nextExecution: number;
    readonly module: string;
    readonly action: string;

}