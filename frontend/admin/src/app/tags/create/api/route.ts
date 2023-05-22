import { NextResponse } from "next/server";

export async function POST(request: Request) {
  // TODO: handle submitted form data
  const data = await request.json();

  return NextResponse.json({ success: true });
}
