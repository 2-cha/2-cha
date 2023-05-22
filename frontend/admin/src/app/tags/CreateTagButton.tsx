'use client';

import { Button, Link } from '@chakra-ui/react';
import NextLink from 'next/link';

export default function CreateTagButton() {
  return (
    <Link as={NextLink} href="/tags/create">
      <Button m={4} colorScheme='teal'>
        Create Tag
      </Button>
    </Link>
  );
}
