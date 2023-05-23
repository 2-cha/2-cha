"use client";

import NextLink from "next/link";
import {
  Grid,
  GridItem,
  HStack,
  Link,
  Button,
  useDisclosure,
  Drawer,
  DrawerContent,
  DrawerBody,
  DrawerCloseButton,
  DrawerOverlay,
} from "@chakra-ui/react";
import { HamburgerIcon } from "@chakra-ui/icons";
import NavBar from "./NavBar";
import { isLoggedIn } from "@/lib/api";

export default async function MainLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  const { onOpen, isOpen, onClose } = useDisclosure({ id: "navbar" });
  const isLogged = await isLoggedIn();

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
        <HStack h="100%" mx={5} justify={{ base: "space-between", md: "end" }}>
          <Button display={{ base: "block", md: "none" }} onClick={onOpen}>
            <HamburgerIcon />
          </Button>
          <NavBarDrawer isOpen={isOpen} onClose={onClose} />
          <HStack gap={4}>
            <Link as={NextLink} href="/" colorScheme="teal">
              Home
            </Link>
            {isLogged ? (
              <Button colorScheme="red">Logout</Button>
            ) : (
              <Link as={NextLink} href="/login" colorScheme="teal">
                Login
              </Link>
            )}
          </HStack>
        </HStack>
      </GridItem>
      <GridItem
        gridArea="nav"
        borderRight="1px"
        borderColor="gray.300"
        display={{ base: "none", md: "block" }}
      >
        <NavBar />
      </GridItem>
      <GridItem gridArea="main" m={6}>
        {children}
      </GridItem>
    </Grid>
  );
}

function NavBarDrawer({
  isOpen,
  onClose,
}: {
  isOpen: boolean;
  onClose: () => void;
}) {
  return (
    <Drawer isOpen={isOpen} placement="left" onClose={onClose}>
      <DrawerOverlay />
      <DrawerContent>
        <DrawerCloseButton />
        <DrawerBody>
          <NavBar />
        </DrawerBody>
      </DrawerContent>
    </Drawer>
  );
}
