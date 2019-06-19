export enum FileType {
  TRAIN = 'TRAIN',
  TEST = 'TEST'
}

export namespace FileTypeParser {
  function isIndex(key):boolean {
    const n = ~~Number(key);
    return String(n) === key && n >= 0;
  }

  const _names:string[] = Object
    .keys(FileType)
    .filter(key => !isIndex(key));

  const _indices:number[] = Object
    .keys(FileType)
    .filter(key => isIndex(key))
    .map(index => Number(index));

  export function names():string[] {
    return _names;
  }

  export function indices():number[] {
    return _indices;
  }
}
