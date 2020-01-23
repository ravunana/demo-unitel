import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Factura } from 'app/shared/model/facturacao/factura.model';
import { FacturaService } from './factura.service';
import { FacturaComponent } from './factura.component';
import { FacturaDetailComponent } from './factura-detail.component';
import { FacturaUpdateComponent } from './factura-update.component';
import { FacturaDeletePopupComponent } from './factura-delete-dialog.component';
import { IFactura } from 'app/shared/model/facturacao/factura.model';

@Injectable({ providedIn: 'root' })
export class FacturaResolve implements Resolve<IFactura> {
  constructor(private service: FacturaService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IFactura> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Factura>) => response.ok),
        map((factura: HttpResponse<Factura>) => factura.body)
      );
    }
    return of(new Factura());
  }
}

export const facturaRoute: Routes = [
  {
    path: '',
    component: FacturaComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.facturacaoFactura.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: FacturaDetailComponent,
    resolve: {
      factura: FacturaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.facturacaoFactura.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: FacturaUpdateComponent,
    resolve: {
      factura: FacturaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.facturacaoFactura.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: FacturaUpdateComponent,
    resolve: {
      factura: FacturaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.facturacaoFactura.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const facturaPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: FacturaDeletePopupComponent,
    resolve: {
      factura: FacturaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.facturacaoFactura.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
