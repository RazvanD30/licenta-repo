export enum StatusInternal {
  IGNORE,
  WATCH,
  BREAKPOINT,
  INPUT,
  WAIT
}


export class Status {

  constructor(status: StatusInternal) {
    this.status = status;
  }

  public static UNDER_WATCH = new Status(StatusInternal.WATCH);
  public static IGNORED = new Status(StatusInternal.IGNORE);
  public static BREAKPOINT = new Status(StatusInternal.BREAKPOINT);
  public static WAIT = new Status(StatusInternal.WAIT);
  public static INPUT = new Status(StatusInternal.INPUT);
  private readonly status: StatusInternal;

  public fromInternal(statusInternal: StatusInternal): Status {
    switch (statusInternal) {
      case StatusInternal.WATCH:
        return Status.UNDER_WATCH;
      case StatusInternal.IGNORE:
        return Status.IGNORED;
      case StatusInternal.BREAKPOINT:
        return Status.BREAKPOINT;
      case StatusInternal.WAIT:
        return Status.WAIT;
      case StatusInternal.INPUT:
        return Status.INPUT;
      default:
        return Status.IGNORED;
    }
  }

  public isWaiting(): boolean {
    return this.status === StatusInternal.BREAKPOINT || this.status === StatusInternal.WAIT;
  }

  public isUnderWatch(): boolean {
    return this.status === StatusInternal.WATCH;
  }

  public isIgnored(): boolean {
    return this.status === StatusInternal.IGNORE;
  }

  public isBreakpoint(): boolean {
    return this.status === StatusInternal.BREAKPOINT;
  }

  public isInput(): boolean {
    return this.status === StatusInternal.INPUT;
  }
}
