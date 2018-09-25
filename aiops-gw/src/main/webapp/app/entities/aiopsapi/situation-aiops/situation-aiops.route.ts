import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { SituationAiops } from 'app/shared/model/aiopsapi/situation-aiops.model';
import { SituationAiopsService } from './situation-aiops.service';
import { SituationAiopsComponent } from './situation-aiops.component';
import { SituationAiopsDetailComponent } from './situation-aiops-detail.component';
import { SituationAiopsUpdateComponent } from './situation-aiops-update.component';
import { SituationAiopsDeletePopupComponent } from './situation-aiops-delete-dialog.component';
import { ISituationAiops } from 'app/shared/model/aiopsapi/situation-aiops.model';

@Injectable({ providedIn: 'root' })
export class SituationAiopsResolve implements Resolve<ISituationAiops> {
    constructor(private service: SituationAiopsService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((situation: HttpResponse<SituationAiops>) => situation.body));
        }
        return of(new SituationAiops());
    }
}

export const situationRoute: Routes = [
    {
        path: 'situation-aiops',
        component: SituationAiopsComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'aiopsgwApp.aiopsapiSituation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'situation-aiops/:id/view',
        component: SituationAiopsDetailComponent,
        resolve: {
            situation: SituationAiopsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'aiopsgwApp.aiopsapiSituation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'situation-aiops/new',
        component: SituationAiopsUpdateComponent,
        resolve: {
            situation: SituationAiopsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'aiopsgwApp.aiopsapiSituation.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'situation-aiops/:id/edit',
        component: SituationAiopsUpdateComponent,
        resolve: {
            situation: SituationAiopsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'aiopsgwApp.aiopsapiSituation.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const situationPopupRoute: Routes = [
    {
        path: 'situation-aiops/:id/delete',
        component: SituationAiopsDeletePopupComponent,
        resolve: {
            situation: SituationAiopsResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'aiopsgwApp.aiopsapiSituation.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
