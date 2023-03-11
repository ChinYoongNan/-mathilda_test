export default class ListRequest {
    page: number;
    size: number;
    total: number;
    query: string;

    constructor(request: {page?: number, size?: number, query?: string; } = {page: 1, size: 5}) {
      this.page = request.page || 1;
      this.size = request.size || 10;
      this.total = 10;
      this.query = request.query || '';
    }
  }

