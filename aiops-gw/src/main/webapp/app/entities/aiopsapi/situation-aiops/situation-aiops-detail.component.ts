import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISituationAiops } from 'app/shared/model/aiopsapi/situation-aiops.model';

@Component({
    selector: 'jhi-situation-aiops-detail',
    templateUrl: './situation-aiops-detail.component.html'
})
export class SituationAiopsDetailComponent implements OnInit {
    situation: ISituationAiops;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ situation }) => {
            this.situation = situation;
        });
    }

    previousState() {
        window.history.back();
    }
}
