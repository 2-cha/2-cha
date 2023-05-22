'use client';

import NextLink from 'next/link';
import { Grid, GridItem, HStack, Link, Button } from '@chakra-ui/react';
import NavBar from './NavBar';

export default function MainLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <Grid
      templateAreas={{
        base: `"header header"
               "main  main"`,
        md: `"header header"
             "nav    main"`,
      }}
      gridTemplateRows="50px 1fr"
      gridTemplateColumns="220px 1fr"
      minH="100vh"
    >
      <GridItem gridArea="header" borderBottom="1px" borderColor="gray.300">
        <HStack h="100%" mx={5} gap={5} justify="end">
          <Link as={NextLink} href="/" colorScheme="teal">
            Home
          </Link>
          <Button colorScheme="red">Logout</Button>
        </HStack>
      </GridItem>
      <GridItem
        gridArea="nav"
        borderRight="1px"
        borderColor="gray.300"
      >
        <NavBar />
      </GridItem>
      <GridItem gridArea="main" m={6}>
        {children}
      </GridItem>
    </Grid>
  );
}
