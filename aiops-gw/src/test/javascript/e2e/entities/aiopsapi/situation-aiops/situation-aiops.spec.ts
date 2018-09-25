/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../../page-objects/jhi-page-objects';

import { SituationComponentsPage, SituationDeleteDialog, SituationUpdatePage } from './situation-aiops.page-object';

const expect = chai.expect;

describe('Situation e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let situationUpdatePage: SituationUpdatePage;
    let situationComponentsPage: SituationComponentsPage;
    let situationDeleteDialog: SituationDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Situations', async () => {
        await navBarPage.goToEntity('situation-aiops');
        situationComponentsPage = new SituationComponentsPage();
        expect(await situationComponentsPage.getTitle()).to.eq('aiopsgwApp.aiopsapiSituation.home.title');
    });

    it('should load create Situation page', async () => {
        await situationComponentsPage.clickOnCreateButton();
        situationUpdatePage = new SituationUpdatePage();
        expect(await situationUpdatePage.getPageTitle()).to.eq('aiopsgwApp.aiopsapiSituation.home.createOrEditLabel');
        await situationUpdatePage.cancel();
    });

    it('should create and save Situations', async () => {
        const nbButtonsBeforeCreate = await situationComponentsPage.countDeleteButtons();

        await situationComponentsPage.clickOnCreateButton();
        await situationUpdatePage.setNameInput('name');
        expect(await situationUpdatePage.getNameInput()).to.eq('name');
        await situationUpdatePage.setTypeInput('type');
        expect(await situationUpdatePage.getTypeInput()).to.eq('type');
        await situationUpdatePage.save();
        expect(await situationUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await situationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Situation', async () => {
        const nbButtonsBeforeDelete = await situationComponentsPage.countDeleteButtons();
        await situationComponentsPage.clickOnLastDeleteButton();

        situationDeleteDialog = new SituationDeleteDialog();
        expect(await situationDeleteDialog.getDialogTitle()).to.eq('aiopsgwApp.aiopsapiSituation.delete.question');
        await situationDeleteDialog.clickOnConfirmButton();

        expect(await situationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
