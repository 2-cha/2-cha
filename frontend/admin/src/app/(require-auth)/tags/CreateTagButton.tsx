'use client';

import { Button, Link } from '@chakra-ui/react';
import { AddIcon } from '@chakra-ui/icons';
import NextLink from 'next/link';

export default function CreateTagButton() {
  return (
    <Link as={NextLink} href="/tags/create">
      <Button leftIcon={<AddIcon />} m={4} colorScheme='teal'>
        Create Tag
      </Button>
    </Link>
  );
}
