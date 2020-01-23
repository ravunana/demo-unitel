import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IProduto, Produto } from 'app/shared/model/estoque/produto.model';
import { ProdutoService } from './produto.service';

@Component({
  selector: 'jhi-produto-update',
  templateUrl: './produto-update.component.html'
})
export class ProdutoUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    nome: [null, [Validators.required]],
    numero: [null, []],
    estoque: [null, [Validators.required, Validators.min(1)]],
    preco: [null, [Validators.required, Validators.min(0)]]
  });

  constructor(protected produtoService: ProdutoService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ produto }) => {
      this.updateForm(produto);
    });
  }

  updateForm(produto: IProduto) {
    this.editForm.patchValue({
      id: produto.id,
      nome: produto.nome,
      numero: produto.numero,
      estoque: produto.estoque,
      preco: produto.preco
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const produto = this.createFromForm();
    if (produto.id !== undefined) {
      this.subscribeToSaveResponse(this.produtoService.update(produto));
    } else {
      this.subscribeToSaveResponse(this.produtoService.create(produto));
    }
  }

  private createFromForm(): IProduto {
    return {
      ...new Produto(),
      id: this.editForm.get(['id']).value,
      nome: this.editForm.get(['nome']).value,
      numero: this.editForm.get(['numero']).value,
      estoque: this.editForm.get(['estoque']).value,
      preco: this.editForm.get(['preco']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduto>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
