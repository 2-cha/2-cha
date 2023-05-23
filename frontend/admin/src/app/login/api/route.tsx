import { isLoggedIn, login } from "@/lib/api/auth";

export async function GET() {
  if (await isLoggedIn()) {
    return new Response(null, { status: 204 });
  }
  return new Response(null, { status: 401 });
}

export async function POST(request: Request) {
  try {
    const data = await request.json();
    await login(data);

    return new Response(null, { status: 204 });
  } catch {
    return new Response(null, { status: 401 });
  }
}
