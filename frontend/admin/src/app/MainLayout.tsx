'use client';

import { Grid, GridItem } from '@chakra-ui/react';

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
      <GridItem gridArea="header" bg="tomato"></GridItem>
      <GridItem gridArea="nav" bg="tan"></GridItem>
      <GridItem gridArea="main" bg="papayawhip">
        {children}
      </GridItem>
    </Grid>
  );
}
