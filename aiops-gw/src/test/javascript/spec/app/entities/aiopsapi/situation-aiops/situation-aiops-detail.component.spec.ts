/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AiopsgwTestModule } from '../../../../test.module';
import { SituationAiopsDetailComponent } from 'app/entities/aiopsapi/situation-aiops/situation-aiops-detail.component';
import { SituationAiops } from 'app/shared/model/aiopsapi/situation-aiops.model';

describe('Component Tests', () => {
    describe('SituationAiops Management Detail Component', () => {
        let comp: SituationAiopsDetailComponent;
        let fixture: ComponentFixture<SituationAiopsDetailComponent>;
        const route = ({ data: of({ situation: new SituationAiops(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AiopsgwTestModule],
                declarations: [SituationAiopsDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(SituationAiopsDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SituationAiopsDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.situation).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
