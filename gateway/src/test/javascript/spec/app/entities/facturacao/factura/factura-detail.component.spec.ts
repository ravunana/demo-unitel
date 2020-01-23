import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { FacturaDetailComponent } from 'app/entities/facturacao/factura/factura-detail.component';
import { Factura } from 'app/shared/model/facturacao/factura.model';

describe('Component Tests', () => {
  describe('Factura Management Detail Component', () => {
    let comp: FacturaDetailComponent;
    let fixture: ComponentFixture<FacturaDetailComponent>;
    const route = ({ data: of({ factura: new Factura(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [FacturaDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(FacturaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FacturaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.factura).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
