/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { AiopsgwTestModule } from '../../../../test.module';
import { SituationAiopsUpdateComponent } from 'app/entities/aiopsapi/situation-aiops/situation-aiops-update.component';
import { SituationAiopsService } from 'app/entities/aiopsapi/situation-aiops/situation-aiops.service';
import { SituationAiops } from 'app/shared/model/aiopsapi/situation-aiops.model';

describe('Component Tests', () => {
    describe('SituationAiops Management Update Component', () => {
        let comp: SituationAiopsUpdateComponent;
        let fixture: ComponentFixture<SituationAiopsUpdateComponent>;
        let service: SituationAiopsService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AiopsgwTestModule],
                declarations: [SituationAiopsUpdateComponent]
            })
                .overrideTemplate(SituationAiopsUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(SituationAiopsUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SituationAiopsService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new SituationAiops(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.situation = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new SituationAiops();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.situation = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
