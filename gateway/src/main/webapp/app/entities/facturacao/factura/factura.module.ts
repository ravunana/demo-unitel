import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { FacturaComponent } from './factura.component';
import { FacturaDetailComponent } from './factura-detail.component';
import { FacturaUpdateComponent } from './factura-update.component';
import { FacturaDeletePopupComponent, FacturaDeleteDialogComponent } from './factura-delete-dialog.component';
import { facturaRoute, facturaPopupRoute } from './factura.route';

const ENTITY_STATES = [...facturaRoute, ...facturaPopupRoute];

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    FacturaComponent,
    FacturaDetailComponent,
    FacturaUpdateComponent,
    FacturaDeleteDialogComponent,
    FacturaDeletePopupComponent
  ],
  entryComponents: [FacturaDeleteDialogComponent]
})
export class FacturacaoFacturaModule {}
