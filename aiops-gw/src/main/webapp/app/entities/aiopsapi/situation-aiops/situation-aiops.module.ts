import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AiopsgwSharedModule } from 'app/shared';
import {
    SituationAiopsComponent,
    SituationAiopsDetailComponent,
    SituationAiopsUpdateComponent,
    SituationAiopsDeletePopupComponent,
    SituationAiopsDeleteDialogComponent,
    situationRoute,
    situationPopupRoute
} from './';

const ENTITY_STATES = [...situationRoute, ...situationPopupRoute];

@NgModule({
    imports: [AiopsgwSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        SituationAiopsComponent,
        SituationAiopsDetailComponent,
        SituationAiopsUpdateComponent,
        SituationAiopsDeleteDialogComponent,
        SituationAiopsDeletePopupComponent
    ],
    entryComponents: [
        SituationAiopsComponent,
        SituationAiopsUpdateComponent,
        SituationAiopsDeleteDialogComponent,
        SituationAiopsDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AiopsgwSituationAiopsModule {}
