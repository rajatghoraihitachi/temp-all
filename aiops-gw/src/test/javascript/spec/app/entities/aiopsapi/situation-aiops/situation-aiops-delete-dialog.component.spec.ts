/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { AiopsgwTestModule } from '../../../../test.module';
import { SituationAiopsDeleteDialogComponent } from 'app/entities/aiopsapi/situation-aiops/situation-aiops-delete-dialog.component';
import { SituationAiopsService } from 'app/entities/aiopsapi/situation-aiops/situation-aiops.service';

describe('Component Tests', () => {
    describe('SituationAiops Management Delete Component', () => {
        let comp: SituationAiopsDeleteDialogComponent;
        let fixture: ComponentFixture<SituationAiopsDeleteDialogComponent>;
        let service: SituationAiopsService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AiopsgwTestModule],
                declarations: [SituationAiopsDeleteDialogComponent]
            })
                .overrideTemplate(SituationAiopsDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(SituationAiopsDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SituationAiopsService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
