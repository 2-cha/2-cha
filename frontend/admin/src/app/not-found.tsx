'use client';

import { Center, VStack, Heading, Text } from '@chakra-ui/react';

export default function NotFound() {
  return (
    <Center h="full">
      <VStack>
        <Heading>404</Heading>
        <Text>Not Found</Text>
      </VStack>
    </Center>
  );
}
