import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { ProdutoComponent } from './produto.component';
import { ProdutoDetailComponent } from './produto-detail.component';
import { ProdutoUpdateComponent } from './produto-update.component';
import { ProdutoDeletePopupComponent, ProdutoDeleteDialogComponent } from './produto-delete-dialog.component';
import { produtoRoute, produtoPopupRoute } from './produto.route';

const ENTITY_STATES = [...produtoRoute, ...produtoPopupRoute];

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ProdutoComponent,
    ProdutoDetailComponent,
    ProdutoUpdateComponent,
    ProdutoDeleteDialogComponent,
    ProdutoDeletePopupComponent
  ],
  entryComponents: [ProdutoDeleteDialogComponent]
})
export class EstoqueProdutoModule {}
