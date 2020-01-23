import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { ProdutoDetailComponent } from 'app/entities/estoque/produto/produto-detail.component';
import { Produto } from 'app/shared/model/estoque/produto.model';

describe('Component Tests', () => {
  describe('Produto Management Detail Component', () => {
    let comp: ProdutoDetailComponent;
    let fixture: ComponentFixture<ProdutoDetailComponent>;
    const route = ({ data: of({ produto: new Produto(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [ProdutoDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ProdutoDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProdutoDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.produto).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
