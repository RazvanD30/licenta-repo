export enum StatusInternal {
  UNDER_WATCH,
  IGNORED,
  BREAKPOINT,
  WAIT,
  INPUT
}


export class Status {

  public static UNDER_WATCH = new Status(StatusInternal.UNDER_WATCH);
  public static IGNORED = new Status(StatusInternal.IGNORED);
  public static BREAKPOINT = new Status(StatusInternal.BREAKPOINT);
  public static WAIT = new Status(StatusInternal.WAIT);
  public static INPUT = new Status(StatusInternal.INPUT);
  private readonly status: StatusInternal;

  constructor(status: StatusInternal) {
    this.status = status;
  }

  public isWaiting(): boolean {
    return this.status === StatusInternal.BREAKPOINT || this.status === StatusInternal.WAIT;
  }

  public isUnderWatch(): boolean {
    return this.status === StatusInternal.UNDER_WATCH;
  }

  public isIgnored(): boolean {
    return this.status === StatusInternal.IGNORED;
  }

  public isBreakpoint(): boolean {
    return this.status === StatusInternal.BREAKPOINT;
  }

  public isInput(): boolean {
    return this.status === StatusInternal.INPUT;
  }
}
