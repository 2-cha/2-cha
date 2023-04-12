export async function fetchServer<T = unknown>(
  url: string,
  options: RequestInit = {}
): Promise<T> {
  const response = await fetch(process.env.NEXT_PUBLIC_BASE_API_URL + url, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      // TODO: remove this
      Authorization: `Bearer ${process.env.NEXT_PUBLIC_TEMP_AUTH_TOKEN}`,
      ...options.headers,
    },
  });

  if (!response.ok) {
    const errorMsg = await response.text();
    throw new Error(errorMsg);
  }

  return response.json();
}
