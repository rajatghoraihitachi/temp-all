export interface ISituationAiops {
    id?: number;
    name?: string;
    type?: string;
}

export class SituationAiops implements ISituationAiops {
    constructor(public id?: number, public name?: string, public type?: string) {}
}
