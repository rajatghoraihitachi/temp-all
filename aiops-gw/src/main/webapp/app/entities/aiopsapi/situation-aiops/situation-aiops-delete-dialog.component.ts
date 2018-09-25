import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISituationAiops } from 'app/shared/model/aiopsapi/situation-aiops.model';
import { SituationAiopsService } from './situation-aiops.service';

@Component({
    selector: 'jhi-situation-aiops-delete-dialog',
    templateUrl: './situation-aiops-delete-dialog.component.html'
})
export class SituationAiopsDeleteDialogComponent {
    situation: ISituationAiops;

    constructor(
        private situationService: SituationAiopsService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.situationService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'situationListModification',
                content: 'Deleted an situation'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-situation-aiops-delete-popup',
    template: ''
})
export class SituationAiopsDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ situation }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SituationAiopsDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.situation = situation;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
