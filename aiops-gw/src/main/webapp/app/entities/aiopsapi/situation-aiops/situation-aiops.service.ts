import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISituationAiops } from 'app/shared/model/aiopsapi/situation-aiops.model';

type EntityResponseType = HttpResponse<ISituationAiops>;
type EntityArrayResponseType = HttpResponse<ISituationAiops[]>;

@Injectable({ providedIn: 'root' })
export class SituationAiopsService {
    private resourceUrl = SERVER_API_URL + 'aiopsapi/api/situations';
    private resourceSearchUrl = SERVER_API_URL + 'aiopsapi/api/_search/situations';

    constructor(private http: HttpClient) {}

    create(situation: ISituationAiops): Observable<EntityResponseType> {
        return this.http.post<ISituationAiops>(this.resourceUrl, situation, { observe: 'response' });
    }

    update(situation: ISituationAiops): Observable<EntityResponseType> {
        return this.http.put<ISituationAiops>(this.resourceUrl, situation, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISituationAiops>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISituationAiops[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISituationAiops[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
