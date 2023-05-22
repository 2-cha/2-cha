'use client';

import NextLink from 'next/link';
import { VStack, Link, Button } from '@chakra-ui/react';
import { usePathname } from 'next/navigation';

const links = [
  {
    name: 'Tag',
    url: '/tags',
  },
];

export default function NavBar() {
  const pathname = usePathname();

  return (
    <VStack align="start" p={6}>
      {links.map((link) => (
        <Link key={link.url} as={NextLink} href={link.url} w='100%'>
          <Button
            color={pathname.startsWith(link.url) ? 'teal.500' : 'gray.600'}
            bg="transparent"
            w="100%"
            justifyContent="start"
          >
            {link.name}
          </Button>
        </Link>
      ))}
    </VStack>
  );
}
