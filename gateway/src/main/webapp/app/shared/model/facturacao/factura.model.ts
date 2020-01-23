import { Moment } from 'moment';
import { Tipo } from 'app/shared/model/enumerations/tipo.model';

export interface IFactura {
  id?: number;
  tipo?: Tipo;
  beneficiario?: string;
  data?: Moment;
  numero?: string;
  produtoCodigo?: string;
  produtoNome?: string;
  produtoPreco?: number;
  produtoQuantidade?: number;
}

export class Factura implements IFactura {
  constructor(
    public id?: number,
    public tipo?: Tipo,
    public beneficiario?: string,
    public data?: Moment,
    public numero?: string,
    public produtoCodigo?: string,
    public produtoNome?: string,
    public produtoPreco?: number,
    public produtoQuantidade?: number
  ) {}
}
