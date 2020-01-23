export interface IProduto {
  id?: number;
  nome?: string;
  numero?: string;
  estoque?: number;
  preco?: number;
}

export class Produto implements IProduto {
  constructor(public id?: number, public nome?: string, public numero?: string, public estoque?: number, public preco?: number) {}
}
