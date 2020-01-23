import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'produto',
        loadChildren: () => import('./estoque/produto/produto.module').then(m => m.EstoqueProdutoModule)
      },
      {
        path: 'factura',
        loadChildren: () => import('./facturacao/factura/factura.module').then(m => m.FacturacaoFacturaModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class GatewayEntityModule {}
