import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ISituationAiops } from 'app/shared/model/aiopsapi/situation-aiops.model';
import { SituationAiopsService } from './situation-aiops.service';

@Component({
    selector: 'jhi-situation-aiops-update',
    templateUrl: './situation-aiops-update.component.html'
})
export class SituationAiopsUpdateComponent implements OnInit {
    private _situation: ISituationAiops;
    isSaving: boolean;

    constructor(private situationService: SituationAiopsService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ situation }) => {
            this.situation = situation;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.situation.id !== undefined) {
            this.subscribeToSaveResponse(this.situationService.update(this.situation));
        } else {
            this.subscribeToSaveResponse(this.situationService.create(this.situation));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISituationAiops>>) {
        result.subscribe((res: HttpResponse<ISituationAiops>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
    get situation() {
        return this._situation;
    }

    set situation(situation: ISituationAiops) {
        this._situation = situation;
    }
}
