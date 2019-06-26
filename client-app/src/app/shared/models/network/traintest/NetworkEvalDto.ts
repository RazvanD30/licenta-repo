export interface NetworkEvalDto {
  accuracy: number;
  precision: number;
  f1Score: number;
  recall: number;
  previousEval: NetworkEvalDto;
}
