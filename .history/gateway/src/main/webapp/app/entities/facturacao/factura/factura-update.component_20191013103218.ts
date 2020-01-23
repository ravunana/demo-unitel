import { Produto, IProduto } from 'app/shared/model/estoque/produto.model';
import { ProdutoService } from 'app/entities/estoque/produto/produto.service';
import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IFactura, Factura } from 'app/shared/model/facturacao/factura.model';
import { FacturaService } from './factura.service';
import { CurrencyPipe } from '@angular/common';

@Component({
  selector: 'jhi-factura-update',
  templateUrl: './factura-update.component.html'
})
export class FacturaUpdateComponent implements OnInit {
  isSaving: boolean;
  produtos: IProduto[] = [];
  produto: IProduto = new Produto();

  editForm = this.fb.group({
    id: [],
    tipo: [null, [Validators.required]],
    beneficiario: [null, [Validators.required]],
    data: [],
    numero: [null, []],
    produtoCodigo: [this.produto.numero, [Validators.required]],
    produtoNome: [this.produto.nome, [Validators.required]],
    produtoPreco: [this.produto.preco, [Validators.required, Validators.min(0)]],
    produtoQuantidade: [55, [Validators.required, Validators.min(1)]]
  });

  constructor(protected facturaService: FacturaService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder, private produtoService: ProdutoService) {
  }

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ factura }) => {
      this.updateForm(factura);
    });

    this.editForm.valueChanges.subscribe( value=> {

    } );

    this.produtoService
      .query()
      .subscribe((res: HttpResponse<IProduto[]>) => this.paginateProdutos(res.body));

      this.onValueChanges();

  }

  onValueChanges(): void  {
    this.editForm.valueChanges.subscribe( value => {
      console.log( value );
    } )
  }

  updateForm(factura: IFactura) {
    this.editForm.patchValue({
      id: factura.id,
      tipo: factura.tipo,
      beneficiario: factura.beneficiario,
      data: factura.data != null ? factura.data.format(DATE_TIME_FORMAT) : null,
      numero: factura.numero,
      produtoCodigo: this.produto.numero,
      produtoNome: this.produto.nome,
      produtoPreco: this.produto.preco,
      produtoQuantidade: factura.produtoQuantidade
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const factura = this.createFromForm();
    if (factura.id !== undefined) {
      this.subscribeToSaveResponse(this.facturaService.update(factura));
    } else {
      this.subscribeToSaveResponse(this.facturaService.create(factura));
    }
  }

  private createFromForm(): IFactura {
    return {
      ...new Factura(),
      id: this.editForm.get(['id']).value,
      tipo: this.editForm.get(['tipo']).value,
      beneficiario: this.editForm.get(['beneficiario']).value,
      data: this.editForm.get(['data']).value != null ? moment(this.editForm.get(['data']).value, DATE_TIME_FORMAT) : undefined,
      numero: this.editForm.get(['numero']).value,
      produtoCodigo: this.editForm.get(['produtoCodigo']).value,
      produtoNome: this.editForm.get(['produtoNome']).value,
      produtoPreco: this.editForm.get(['produtoPreco']).value,
      produtoQuantidade: this.editForm.get(['produtoQuantidade']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFactura>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }

  protected paginateProdutos(data: IProduto[]) {
    for (let i = 0; i < data.length; i++) {
      this.produtos.push(data[i]);
    }
  }

  getIdProduto(id: number) {
     this.produto = this.produtos.find( p=> p.id == id );
     this.editForm.patchValue({
      produtoCodigo: this.produto.numero,
      produtoNome: this.produto.nome,
      produtoPreco: this.produto.preco
    });
  }

}
