export enum Activation {
  CUBE = 'CUBE',
  ELU = 'ELU',
  HARDSIGMOID = 'HARDSIGMOID',
  HARDTANH = 'HARDTANH',
  IDENTITY = 'IDENTITY',
  LEAKYRELU = 'LEAKYRELU',
  RATIONALTANH = 'RATIONALTANH',
  RELU = 'RELU',
  RELU6 = 'RELU6',
  RRELU = 'RRELU',
  SIGMOID = 'SIGMOID',
  SOFTMAX = 'SOFTMAX',
  SOFTPLUS = 'SOFTPLUS',
  SOFTSIGN = 'SOFTSIGN',
  TANH = 'TANH',
  RECTIFIEDTANH = 'RECTIFIEDTANH',
  SELU = 'SELU',
  SWISH = 'SWISH',
  THRESHOLDEDRELU = 'THRESHOLDEDRELU'
}

export namespace ActivationParser {
  function isIndex(key):boolean {
    const n = ~~Number(key);
    return String(n) === key && n >= 0;
  }

  const _names:string[] = Object
    .keys(Activation)
    .filter(key => !isIndex(key));

  const _indices:number[] = Object
    .keys(Activation)
    .filter(key => isIndex(key))
    .map(index => Number(index));

  export function names():string[] {
    return _names;
  }

  export function indices():number[] {
    return _indices;
  }
}
