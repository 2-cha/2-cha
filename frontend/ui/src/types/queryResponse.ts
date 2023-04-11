interface SuccessResponse<Data> {
  success: true;
  status: string;
  data: Data;
}

interface ErrorResponse {
  success: false;
  code: string;
  message: string;
  status: string;
  timestamp?: string;
}

export type QueryResponse<Data> = SuccessResponse<Data> | ErrorResponse;
