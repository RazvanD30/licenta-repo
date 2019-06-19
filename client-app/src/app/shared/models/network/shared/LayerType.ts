export enum LayerType {
  INPUT = 'INPUT',
  OUTPUT = 'OUTPUT',
  DENSE = 'DENSE'
}

export namespace LayerTypeParser {
  function isIndex(key):boolean {
    const n = ~~Number(key);
    return String(n) === key && n >= 0;
  }

  const _names:string[] = Object
    .keys(LayerType)
    .filter(key => !isIndex(key));

  const _indices:number[] = Object
    .keys(LayerType)
    .filter(key => isIndex(key))
    .map(index => Number(index));

  export function names():string[] {
    return _names;
  }

  export function indices():number[] {
    return _indices;
  }
}
