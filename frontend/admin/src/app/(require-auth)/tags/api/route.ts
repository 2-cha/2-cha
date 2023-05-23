import { getAllTags } from '@/lib/api';
import { NextResponse } from 'next/server';

export async function GET() {
  return NextResponse.json(await getAllTags());
}
